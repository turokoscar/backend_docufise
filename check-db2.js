const mysql = require('mysql2/promise');
async function test() {
  const conn = await mysql.createConnection('mysql://root:12345678@localhost:3306/BD_DOCUFISE_DEV');
  
  console.log("=== rol_menu columns ===");
  const [cols] = await conn.execute("SHOW COLUMNS FROM rol_menu");
  console.log(cols.map(c => c.Field));
  
  console.log("\n=== rol_menu data ===");
  const [rows] = await conn.execute("SELECT * FROM rol_menu");
  console.log(JSON.stringify(rows, null, 2));
  
  conn.end();
}
test().catch(console.error);
