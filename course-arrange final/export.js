const puppeteer = require('puppeteer-core');
const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

// Configuration
const PORT = 8080;
const BASE_URL = `http://localhost:${PORT}`;

// List of pages to export
const pages = [
    // Figures
    'figure1_1', 'figure2_1', 'figure4_1', 'figure4_2', 'figure4_3',
    'figure4_4', 'figure4_5', 'figure4_6', 'figure4_7', 'figure5_1',
    'figure5_2', 'figure5_3', 'figure6_1',
    // Tables
    'table1_2', 'table2_3', 'table3_2', 'table3_3', 'table6_1',
    'table6_2', 'table6_2b', 'table6_3', 'table6_3b', 'table6_4',
    'table6_5', 'table6_6', 'experiment'
];

// Output directories
const PNG_DIR = path.join(__dirname, 'export', 'png');
const CSV_DIR = path.join(__dirname, 'export', 'csv');

// Create directories
function ensureDirectories() {
    if (!fs.existsSync(PNG_DIR)) fs.mkdirSync(PNG_DIR, { recursive: true });
    if (!fs.existsSync(CSV_DIR)) fs.mkdirSync(CSV_DIR, { recursive: true });
    console.log('Output directories created:');
    console.log('  PNG:', PNG_DIR);
    console.log('  CSV:', CSV_DIR);
}

// Parse HTML file and extract tables to CSV
function htmlToCsv(htmlPath) {
    try {
        const html = fs.readFileSync(htmlPath, 'utf-8');
        const dom = new JSDOM(html);
        const document = dom.window.document;

        const tables = document.querySelectorAll('table.data-table');
        if (tables.length === 0) {
            console.log(`  No tables found in ${path.basename(htmlPath)}`);
            return;
        }

        tables.forEach((table, tableIndex) => {
            const rows = [];
            const tableHeaders = [];

            // Get headers
            const thead = table.querySelector('thead');
            if (thead) {
                const headerCells = thead.querySelectorAll('th');
                headerCells.forEach(th => {
                    tableHeaders.push(th.textContent.trim());
                });
            }

            // Get data rows
            const tbody = table.querySelector('tbody');
            if (tbody) {
                const dataRows = tbody.querySelectorAll('tr');
                dataRows.forEach(tr => {
                    const row = [];
                    const cells = tr.querySelectorAll('td, th');
                    cells.forEach(cell => {
                        row.push(cell.textContent.trim());
                    });
                    if (row.length > 0) {
                        rows.push(row);
                    }
                });
            }

            // Create CSV content
            let csvContent = '';
            if (tableHeaders.length > 0) {
                csvContent += tableHeaders.map(h => `"${h}"`).join(',') + '\n';
            }
            rows.forEach(row => {
                csvContent += row.map(cell => `"${cell}"`).join(',') + '\n';
            });

            // Save CSV file
            const baseName = path.basename(htmlPath, '.html');
            const csvFileName = tableIndex === 0 ? `${baseName}.csv` : `${baseName}_table${tableIndex + 1}.csv`;
            const csvPath = path.join(CSV_DIR, csvFileName);
            fs.writeFileSync(csvPath, csvContent, 'utf-8');
            console.log(`  Created: ${csvFileName}`);
        });
    } catch (error) {
        console.error(`  Error parsing ${htmlPath}:`, error.message);
    }
}

// Export all tables to CSV
async function exportAllToCsv() {
    console.log('\n--- Exporting Tables to CSV ---');
    const staticDir = path.join(__dirname, 'src', 'main', 'resources', 'static');

    const tableExtensions = ['table1_2', 'table2_3', 'table3_2', 'table3_3', 'table6_1',
        'table6_2', 'table6_2b', 'table6_3', 'table6_3b', 'table6_4', 'table6_5', 'table6_6'];

    tableExtensions.forEach(name => {
        const htmlPath = path.join(staticDir, `${name}.html`);
        if (fs.existsSync(htmlPath)) {
            htmlToCsv(htmlPath);
        }
    });
}

// Take screenshot of a page
async function takeScreenshot(browser, pageName) {
    try {
        const page = await browser.newPage();
        const url = `${BASE_URL}/${pageName}.html`;
        console.log(`  Loading: ${url}`);

        await page.goto(url, { waitUntil: 'networkidle0', timeout: 30000 });

        // Wait a bit for any dynamic content
        await new Promise(resolve => setTimeout(resolve, 2000));

        // Take screenshot
        const screenshotPath = path.join(PNG_DIR, `${pageName}.png`);
        await page.screenshot({
            path: screenshotPath,
            fullPage: true
        });
        console.log(`  Saved: ${pageName}.png`);

        await page.close();
    } catch (error) {
        console.error(`  Error capturing ${pageName}:`, error.message);
    }
}

// Export all pages to PNG
async function exportAllToPng() {
    console.log('\n--- Exporting Pages to PNG ---');

    // Find Edge browser executable
    const edgePaths = [
        'C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe',
        'C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe'
    ];

    let edgePath = edgePaths.find(p => fs.existsSync(p));
    if (!edgePath) {
        console.error('Edge browser not found!');
        return;
    }

    console.log('Using Edge at:', edgePath);

    const browser = await puppeteer.launch({
        executablePath: edgePath,
        headless: true,
        args: ['--no-sandbox', '--disable-setuid-sandbox']
    });

    try {
        for (const pageName of pages) {
            await takeScreenshot(browser, pageName);
        }
    } finally {
        await browser.close();
    }
}

// Main function
async function main() {
    console.log('========================================');
    console.log('  Export Tool - HTML to PNG & CSV');
    console.log('========================================\n');

    ensureDirectories();

    // Export to CSV first (doesn't need server)
    await exportAllToCsv();

    // Export to PNG (needs server running)
    console.log('\nNote: Make sure the server is running on port', PORT);
    await exportAllToPng();

    console.log('\n========================================');
    console.log('  Export Complete!');
    console.log('========================================');
}

main().catch(console.error);
