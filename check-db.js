const mysql = require('mysql2/promise');
async function test() {
  const conn = await mysql.createConnection('mysql://root:12345678@localhost:3306/BD_DOCUFISE_DEV');
  
  console.log("=== Tables with 'rol' or 'menu' ===");
  const [tables] = await conn.execute("SHOW TABLES");
  console.log(tables.map(t => Object.values(t)[0]));
  
  console.log("\n=== Menu table columns ===");
  const [cols] = await conn.execute("SHOW COLUMNS FROM menu");
  console.log(cols.map(c => c.Field));
  
  console.log("\n=== Menus data ===");
  const [menus] = await conn.execute("SELECT * FROM menu");
  console.log(JSON.stringify(menus, null, 2));
  
  conn.end();
}
test().catch(console.error);
