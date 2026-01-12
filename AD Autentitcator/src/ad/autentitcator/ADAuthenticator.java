/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ad.autentitcator;
import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;
/**
 *
 * @author rafael.anjos
 */
public class ADAuthenticator {

 public static void main(String[] args) {
        String username = "dosanjos.developer"; // apenas o nome, sem domínio
        String password = "ads2025";
        String domain = "developer"; // exemplo: empresa.local
        String ldapHost = "ldap://192.168.1.99:389"; // exemplo: ldap://192.168.1.10:389

        authenticateUser(domain, username, password, ldapHost);
    }

    public static void authenticateUser(String domain, String username, String password, String ldapHost) {
        String userPrincipal = domain + "\\" + username;
//        String userPrincipal = "developer.local\\dosanjos.developer";

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapHost);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userPrincipal);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            DirContext ctx = new InitialDirContext(env);
            System.out.println("✅ Autenticação realizada com sucesso!");
            ctx.close();
        } catch (AuthenticationException authEx) {
            String message = authEx.getMessage();
            if (message.contains("data 525")) {
                System.err.println("❌ ERRO: Usuário não encontrado (data 525).");
            } else if (message.contains("data 52e")) {
                System.err.println("❌ ERRO: Nome de usuário ou senha incorretos (data 52e).");
            } else if (message.contains("data 532")) {
                System.err.println("❌ ERRO: Senha expirada (data 532).");
            } else if (message.contains("data 533")) {
                System.err.println("❌ ERRO: Conta desativada (data 533).");
            } else if (message.contains("data 775")) {
                System.err.println("❌ ERRO: Conta bloqueada (data 775).");
            } else {
                System.err.println("❌ ERRO de autenticação: " + authEx.getMessage());
            }
        } catch (NamingException e) {
            System.err.println("❌ Erro ao conectar no AD: " + e.getMessage());
        }
    }
    
}

