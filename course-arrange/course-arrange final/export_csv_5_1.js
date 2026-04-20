const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

// Export table5_1 specifically
const staticDir = path.join(__dirname, 'src', 'main', 'resources', 'static');
const htmlPath = path.join(staticDir, 'table5_1.html');
const csvDir = path.join(__dirname, 'export', 'csv');

if (fs.existsSync(htmlPath)) {
    const html = fs.readFileSync(htmlPath, 'utf-8');
    const dom = new JSDOM(html);
    const doc = dom.window.document;
    const tables = doc.querySelectorAll('table.data-table');

    tables.forEach((table, idx) => {
        const rows = [];

        // Headers
        const thead = table.querySelector('thead');
        if (thead) {
            const headers = [];
            thead.querySelectorAll('th').forEach(th => headers.push(th.textContent.trim()));
            if (headers.length > 0) rows.push(headers);
        }

        // Body
        const tbody = table.querySelector('tbody');
        if (tbody) {
            tbody.querySelectorAll('tr').forEach(tr => {
                const row = [];
                tr.querySelectorAll('td').forEach(cell => row.push(cell.textContent.trim()));
                if (row.length > 0) rows.push(row);
            });
        }

        const content = rows.map(r => r.map(c => `"${c}"`).join(',')).join('\n');
        const outPath = path.join(csvDir, 'table5_1.csv');
        fs.writeFileSync(outPath, content, 'utf-8');
        console.log('Created: table5_1.csv');
    });
} else {
    console.log('File not found: table5_1.html');
}
