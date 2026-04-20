const puppeteer = require('puppeteer-core');
const fs = require('fs');
const path = require('path');

// Configuration
const PORT = 8080;
const BASE_URL = `http://localhost:${PORT}`;

// Original pages to export
const pages = [
    // Figures
    'figure1_1', 'figure2_1', 'figure3_1', 'figure4_1', 'figure4_2', 'figure4_3',
    'figure4_4', 'figure4_5', 'figure4_6', 'figure4_7', 'figure5_1',
    'figure5_2', 'figure5_3', 'figure6_1', 'figure6_2', 'figure6_4', 'figure6_5',
    // Tables
    'table1_1', 'table1_2', 'table2_1', 'table2_2', 'table2_3',
    'table3_1', 'table3_2', 'table3_3',
    'table6_1', 'table6_2', 'table6_2b', 'table6_3', 'table6_3b',
    'table6_4', 'table6_5', 'table6_6'
];

// Output directory
const OUTPUT_DIR = path.join(__dirname, 'export', 'png');

// Create directory
function ensureDir() {
    if (!fs.existsSync(OUTPUT_DIR)) fs.mkdirSync(OUTPUT_DIR, { recursive: true });
    console.log('Output:', OUTPUT_DIR);
}

// Get page dimensions
function getPageSize(pageName) {
    if (pageName.startsWith('table')) return { width: 1200, height: 800 };
    if (pageName.includes('6_1')) return { width: 1200, height: 700 };
    return { width: 1000, height: 700 };
}

// Export to PNG
async function exportToPng() {
    console.log('\n--- Exporting Original Figures ---');

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
        for (const pageName of pages) {
            try {
                const page = await browser.newPage();
                const url = `${BASE_URL}/${pageName}.html`;
                console.log(`  Processing: ${pageName}`);

                const size = getPageSize(pageName);
                await page.setViewport({ width: size.width, height: size.height, deviceScaleFactor: 2 });

                await page.goto(url, { waitUntil: 'networkidle0', timeout: 30000 });
                await new Promise(resolve => setTimeout(resolve, 1000));

                const outPath = path.join(OUTPUT_DIR, `${pageName}.png`);
                await page.screenshot({ path: outPath, type: 'png', fullPage: false });
                console.log(`  Saved: ${pageName}.png`);

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
    console.log('  Original Figures Export');
    console.log('========================================');
    ensureDir();
    await exportToPng();
    console.log('\nDone!');
}

main().catch(console.error);
