const mysql = require('mysql2/promise');
async function test() {
  const conn = await mysql.createConnection('mysql://root:12345678@localhost:3306/BD_DOCUFISE_DEV');
  const [rows] = await conn.execute('SELECT ide_usuario, txt_nombreUsuario, flg_activo FROM usuario');
  console.log(rows);
  conn.end();
}
test().catch(console.error);
