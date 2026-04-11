const mysql = require('mysql2/promise');
async function fix() {
  const conn = await mysql.createConnection('mysql://root:12345678@localhost:3306/BD_DOCUFISE_DEV');
  
  const toDrop = ['id', 'menu_id', 'rol_id'];
  for (const col of toDrop) {
    try {
      await conn.execute(`ALTER TABLE rol_menu DROP COLUMN \`${col}\``);
      console.log(`Dropped column: ${col}`);
    } catch(e) {
      console.log(`Skipped column ${col}: ${e.message}`);
    }
  }
  
  console.log("\nCurrent columns:");
  const [cols] = await conn.execute("SHOW COLUMNS FROM rol_menu");
  console.log(cols.map(c => c.Field));
  conn.end();
}
fix().catch(console.error);
