const puppeteer = require('puppeteer-core');
const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

// Configuration
const PORT = 8080;
const BASE_URL = `http://localhost:${PORT}`;

// Pages optimized for paper (white background versions)
const paperPages = [
    'figure1_1', 'figure2_1', 'figure4_1', 'figure4_2', 'figure4_3',
    'figure4_4', 'figure4_5', 'figure4_6', 'figure4_7', 'figure5_1',
    'figure5_2', 'figure5_3', 'figure6_1',
    'table1_2', 'table2_3', 'table3_2', 'table3_3', 'table6_1',
    'table6_2', 'table6_2b', 'table6_3', 'table6_3b', 'table6_4',
    'table6_5', 'table6_6'
];

// Output directories
const PNG_DIR = path.join(__dirname, 'export', 'png_sci');
const CSV_DIR = path.join(__dirname, 'export', 'csv');

// SCI paper dimensions (in pixels at 96 DPI)
// Single column: ~85mm = 320px at 96 DPI
// Full width: ~180mm = 680px at 96 DPI
// For 300 DPI: multiply by 3.125
const PAPER_SIZES = {
    single: { width: 1000, height: 1200 },   // Single column figure
    wide: { width: 1600, height: 1000 },     // Wide figure
    table: { width: 1400, height: 1000 }     // Table
};

// Create directories
function ensureDirectories() {
    if (!fs.existsSync(PNG_DIR)) fs.mkdirSync(PNG_DIR, { recursive: true });
    console.log('Output directory:', PNG_DIR);
}

// CSS to inject for white background and paper-friendly styling
const whiteBgCSS = `
    * { -webkit-print-color-adjust: exact !important; print-color-adjust: exact !important; }
    body { background: #ffffff !important; }
    .bg { background: #ffffff !important; }
    .bg::before { display: none !important; }
    .content-box, .tier, .stage-box, .op-card, .tech-card, .cond-card, .params-box, .formula-box, .weight, .param, .stat-card {
        background: #ffffff !important;
        border-color: #333333 !important;
        box-shadow: none !important;
    }
    .header h1, .figure-title, .table-title, .tier-label, .op-title, .tech-title, .cond-title, .params-title, .formula-title, .stage-title, .stat-value {
        color: #000000 !important;
        text-shadow: none !important;
    }
    .header .subtitle, .stage-desc, .tech-item, .cond-item, .step-desc, .footer, .weight-label, .param-label, .stat-label, .param-value {
        color: #333333 !important;
    }
    .component, .gene {
        background: #f5f5f5 !important;
        color: #000000 !important;
        border-color: #999999 !important;
    }
    .gene.mutated { background: #ffcccc !important; }
    .data-table th { background: #f0f0f0 !important; color: #000000 !important; }
    .data-table td { background: #ffffff !important; color: #000000 !important; border-color: #cccccc !important; }
    .highlight { color: #006600 !important; }
    .optimal { background: #e6ffe6 !important; }
    .winner { background: #e6ffe6 !important; }
    .arrow { color: #666666 !important; }
    .tier.presentation { border-color: #0066cc !important; }
    .tier.business { border-color: #6600cc !important; }
    .tier.data { border-color: #008800 !important; }
    .tier.presentation .tier-label { color: #0066cc !important; }
    .tier.business .tier-label { color: #6600cc !important; }
    .tier.data .tier-label { color: #008800 !important; }
    .stage-box.hard { border-color: #cc0000 !important; }
    .stage-box.soft { border-color: #008800 !important; }
    .stage-box.resource { border-color: #6600cc !important; }
    .stage-box.fitness { border-color: #0066cc !important; }
    .stage-box.hard .stage-title { color: #cc0000 !important; }
    .stage-box.soft .stage-title { color: #008800 !important; }
    .stage-box.resource .stage-title { color: #6600cc !important; }
    .stage-box.fitness .stage-title { color: #0066cc !important; }
    .formula .hard { color: #cc0000 !important; }
    .formula .soft { color: #008800 !important; }
    .formula .resource { color: #6600cc !important; }
    .cond-card.early { border-color: #cc0000 !important; }
    .cond-card.normal { border-color: #008800 !important; }
    .cond-card.early .cond-title { color: #cc0000 !important; }
    .cond-card.normal .cond-title { color: #008800 !important; }
    .step-num { background: #0066cc !important; color: #ffffff !important; }
`;

// Inject white background CSS
async function injectPrintStyles(page) {
    await page.addStyleTag({ content: whiteBgCSS });
}

// Parse HTML to CSV
function htmlToCsv(htmlPath) {
    try {
        const html = fs.readFileSync(htmlPath, 'utf-8');
        const dom = new JSDOM(html);
        const document = dom.window.document;

        const tables = document.querySelectorAll('table.data-table');
        if (tables.length === 0) return;

        tables.forEach((table, tableIndex) => {
            const rows = [];
            const tableHeaders = [];

            const thead = table.querySelector('thead');
            if (thead) {
                const headerCells = thead.querySelectorAll('th');
                headerCells.forEach(th => {
                    tableHeaders.push(th.textContent.trim());
                });
            }

            const tbody = table.querySelector('tbody');
            if (tbody) {
                const dataRows = tbody.querySelectorAll('tr');
                dataRows.forEach(tr => {
                    const row = [];
                    const cells = tr.querySelectorAll('td, th');
                    cells.forEach(cell => {
                        row.push(cell.textContent.trim());
                    });
                    if (row.length > 0) rows.push(row);
                });
            }

            let csvContent = '';
            if (tableHeaders.length > 0) {
                csvContent += tableHeaders.map(h => `"${h}"`).join(',') + '\n';
            }
            rows.forEach(row => {
                csvContent += row.map(cell => `"${cell}"`).join(',') + '\n';
            });

            const baseName = path.basename(htmlPath, '.html');
            const csvFileName = tableIndex === 0 ? `${baseName}.csv` : `${baseName}_table${tableIndex + 1}.csv`;
            const csvPath = path.join(CSV_DIR, csvFileName);
            fs.writeFileSync(csvPath, csvContent, 'utf-8');
            console.log(`  Created: ${csvFileName}`);
        });
    } catch (error) {
        console.error(`  Error:`, error.message);
    }
}

// Export tables to CSV
async function exportCsv() {
    console.log('\n--- Exporting Tables to CSV ---');
    const staticDir = path.join(__dirname, 'src', 'main', 'resources', 'static');
    const tableNames = ['table1_2', 'table2_3', 'table3_2', 'table3_3', 'table6_1',
        'table6_2', 'table6_2b', 'table6_3', 'table6_3b', 'table6_4', 'table6_5', 'table6_6'];
    tableNames.forEach(name => {
        const htmlPath = path.join(staticDir, `${name}.html`);
        if (fs.existsSync(htmlPath)) htmlToCsv(htmlPath);
    });
}

// Determine page type for sizing
function getPageSize(pageName) {
    if (pageName.startsWith('table')) return PAPER_SIZES.table;
    if (pageName.startsWith('figure') && pageName.includes('6_1')) return PAPER_SIZES.wide;
    return PAPER_SIZES.single;
}

// Take screenshot with paper-friendly settings
async function takeSciScreenshot(browser, pageName) {
    try {
        const page = await browser.newPage();
        const url = `${BASE_URL}/${pageName}.html`;
        console.log(`  Processing: ${pageName}`);

        const size = getPageSize(pageName);

        // Set viewport for high-quality output
        await page.setViewport({
            width: size.width,
            height: size.height,
            deviceScaleFactor: 3  // 3x for ~288 DPI
        });

        await page.goto(url, { waitUntil: 'networkidle0', timeout: 30000 });

        // Inject white background CSS
        await injectPrintStyles(page);

        // Wait for styles to apply
        await new Promise(resolve => setTimeout(resolve, 500));

        // Take screenshot
        const screenshotPath = path.join(PNG_DIR, `${pageName}.png`);
        await page.screenshot({
            path: screenshotPath,
            type: 'png',
            fullPage: false
        });
        console.log(`  Saved: ${pageName}.png (${size.width}x${size.height} @ 3x)`);

        await page.close();
    } catch (error) {
        console.error(`  Error: ${pageName} -`, error.message);
    }
}

// Export all to SCI-quality PNG
async function exportSciPng() {
    console.log('\n--- Exporting SCI-Quality PNG ---');

    const edgePaths = [
        'C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe',
        'C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe'
    ];
    let edgePath = edgePaths.find(p => fs.existsSync(p));
    if (!edgePath) {
        console.error('Edge not found!');
        return;
    }

    console.log('Using Edge:', edgePath);

    const browser = await puppeteer.launch({
        executablePath: edgePath,
        headless: true,
        args: ['--no-sandbox', '--disable-setuid-sandbox']
    });

    try {
        for (const pageName of paperPages) {
            await takeSciScreenshot(browser, pageName);
        }
    } finally {
        await browser.close();
    }
}

// Main
async function main() {
    console.log('========================================');
    console.log('  SCI Paper Export Tool');
    console.log('  High-resolution PNG + CSV');
    console.log('========================================\n');

    ensureDirectories();
    await exportCsv();
    console.log('\nMake sure server is running on port', PORT);
    await exportSciPng();

    console.log('\n========================================');
    console.log('  Export Complete!');
    console.log('  Output: export/png_sci/');
    console.log('========================================');
}

main().catch(console.error);
