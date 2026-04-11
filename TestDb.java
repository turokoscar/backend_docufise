import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDb {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_DOCUFISE_DEV?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", "root", "12345678");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ide_usuario, txt_nombreUsuario, flg_activo FROM usuario");
            System.out.println("Users in DB:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("ide_usuario") + " | User: '" + rs.getString("txt_nombreUsuario") + "' | Activo: " + rs.getBoolean("flg_activo"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
