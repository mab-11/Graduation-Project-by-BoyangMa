const puppeteer = require('puppeteer-core');
const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

const PORT = 8080;
const BASE_URL = `http://localhost:${PORT}`;

// All table pages to export to CSV (both paper_ and regular versions)
const tablePages = [
    'paper_table1_1', 'paper_table1_2', 'paper_table2_1', 'paper_table2_2', 'paper_table2_3',
    'paper_table3_1', 'paper_table3_2', 'paper_table3_3', 'paper_table6_1', 'paper_table6_2', 'paper_table6_2b',
    'paper_table6_3', 'paper_table6_3b', 'paper_table6_4', 'paper_table6_5', 'paper_table6_6',
    // Also check regular versions
    'table1_2', 'table2_3', 'table3_2', 'table3_3',
    'table6_1', 'table6_2', 'table6_2b', 'table6_3', 'table6_3b', 'table6_4', 'table6_5', 'table6_6'
];

const OUTPUT_DIR = path.join(__dirname, 'export', 'csv');

function ensureDir() {
    if (!fs.existsSync(OUTPUT_DIR)) fs.mkdirSync(OUTPUT_DIR, { recursive: true });
    console.log('CSV Output:', OUTPUT_DIR);
}

// Extract tables from HTML
function extractTables(htmlPath) {
    try {
        const html = fs.readFileSync(htmlPath, 'utf-8');
        const dom = new JSDOM(html);
        const doc = dom.window.document;
        return doc.querySelectorAll('table');
    } catch (e) {
        console.error('Error reading:', e.message);
        return [];
    }
}

// Convert table to CSV
function tableToCsv(table, tableIndex) {
    const rows = [];

    // Headers
    const thead = table.querySelector('thead');
    if (thead) {
        const headers = [];
        thead.querySelectorAll('th').forEach(th => {
            headers.push(th.textContent.trim());
        });
        if (headers.length > 0) rows.push(headers);
    }

    // Body
    const tbody = table.querySelector('tbody');
    if (tbody) {
        tbody.querySelectorAll('tr').forEach(tr => {
            const row = [];
            tr.querySelectorAll('td, th').forEach(cell => {
                row.push(cell.textContent.trim());
            });
            if (row.length > 0) rows.push(row);
        });
    }

    return rows;
}

// Save CSV
function saveCsv(filename, rows) {
    let content = rows.map(row => row.map(cell => `"${cell}"`).join(',')).join('\n');
    fs.writeFileSync(path.join(OUTPUT_DIR, filename), content, 'utf-8');
}

// Export all tables to CSV
function exportAllCsv() {
    console.log('\n--- Exporting Tables to CSV ---');
    const staticDir = path.join(__dirname, 'src', 'main', 'resources', 'static');
    const processed = new Set();

    tablePages.forEach(name => {
        // Try paper_ version first, then regular version
        let htmlPath = path.join(staticDir, `${name}.html`);
        let outName = name.replace('paper_', '');

        if (!fs.existsSync(htmlPath)) {
            // Try without paper_ prefix
            if (name.startsWith('paper_')) {
                htmlPath = path.join(staticDir, `${outName}.html`);
            }
            if (!fs.existsSync(htmlPath)) {
                return;
            }
        }

        // Skip if already processed
        if (processed.has(outName)) return;
        processed.add(outName);

        const tables = extractTables(htmlPath);
        if (tables.length === 0) return;

        tables.forEach((table, idx) => {
            const rows = tableToCsv(table, idx);
            const finalName = idx === 0 ? `${outName}.csv` : `${outName}_${idx + 1}.csv`;
            saveCsv(finalName, rows);
            console.log(`  Created: ${finalName}`);
        });
    });
}

// Main
async function main() {
    console.log('========================================');
    console.log('  CSV Export for Tables');
    console.log('========================================');
    ensureDir();
    exportAllCsv();
    console.log('\nDone!');
}

main();
