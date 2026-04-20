const puppeteer = require('puppeteer-core');
const fs = require('fs');
const path = require('path');

// Configuration
const PORT = 8080;
const BASE_URL = `http://localhost:${PORT}`;

// Paper-style pages
const paperPages = [
    'paper_figure1_1', 'paper_figure2_1', 'paper_figure3_1', 'paper_figure4_1', 'paper_figure4_2',
    'paper_figure4_3', 'paper_figure4_4', 'paper_figure4_5', 'paper_figure4_6',
    'paper_figure4_7', 'paper_figure5_1', 'paper_figure5_2', 'paper_figure5_3',
    'paper_figure6_1', 'paper_table1_1', 'paper_table2_1', 'paper_table2_2', 'paper_table3_1'
];

// Output directory
const OUTPUT_DIR = path.join(__dirname, 'export', 'paper_png');

// Create directory
function ensureDir() {
    if (!fs.existsSync(OUTPUT_DIR)) fs.mkdirSync(OUTPUT_DIR, { recursive: true });
    console.log('Output:', OUTPUT_DIR);
}

// Get page dimensions
function getPageSize(pageName) {
    if (pageName.includes('6_1') || pageName.includes('table')) return { width: 1000, height: 600 };
    return { width: 800, height: 600 };
}

// Export to PNG
async function exportToPng() {
    console.log('\n--- Exporting Paper Figures ---');

    const edgePaths = [
        'C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe',
        'C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe'
    ];
    let edgePath = edgePaths.find(p => fs.existsSync(p));
    if (!edgePath) {
        console.error('Edge not found!');
        return;
    }

    const browser = await puppeteer.launch({
        executablePath: edgePath,
        headless: true,
        args: ['--no-sandbox', '--disable-setuid-sandbox']
    });

    try {
        for (const pageName of paperPages) {
            try {
                const page = await browser.newPage();
                const url = `${BASE_URL}/${pageName}.html`;
                console.log(`  Processing: ${pageName}`);

                const size = getPageSize(pageName);
                await page.setViewport({ width: size.width, height: size.height, deviceScaleFactor: 3 });

                await page.goto(url, { waitUntil: 'networkidle0', timeout: 30000 });
                await new Promise(resolve => setTimeout(resolve, 500));

                const outName = pageName.replace('paper_', '') + '.png';
                const outPath = path.join(OUTPUT_DIR, outName);
                await page.screenshot({ path: outPath, type: 'png', fullPage: false });
                console.log(`  Saved: ${outName}`);

                await page.close();
            } catch (err) {
                console.error(`  Error: ${pageName} -`, err.message);
            }
        }
    } finally {
        await browser.close();
    }
}

// Main
async function main() {
    console.log('========================================');
    console.log('  Paper Figures Export');
    console.log('========================================');
    ensureDir();
    await exportToPng();
    console.log('\nDone!');
}

main().catch(console.error);
