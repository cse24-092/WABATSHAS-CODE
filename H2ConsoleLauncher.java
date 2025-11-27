// [file name]: H2ConsoleLauncher.java
package com.elitebank;

import org.h2.tools.Server;

public class H2ConsoleLauncher {
    public static void main(String[] args) {
        // Try different ports
        int[] ports = {8082, 8083, 8084, 8085, 8086};
        Server server = null;
        
        for (int port : ports) {
            try {
                System.out.println("Trying to start H2 Console on port " + port + "...");
                server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", String.valueOf(port)).start();
                System.out.println("✅ H2 Console started successfully on port: " + port);
                System.out.println("🌐 Open your browser to: http://localhost:" + port);
                break;
            } catch (Exception e) {
                System.out.println("❌ Port " + port + " is in use, trying next port...");
                if (server != null) {
                    server.stop();
                }
            }
        }
        
        if (server == null) {
            System.err.println("❌ Could not start H2 Console on any port. All ports 8082-8086 are in use.");
            return;
        }
        
        System.out.println("\n=== H2 CONNECTION DETAILS ===");
        System.out.println("JDBC URL: jdbc:h2:file:./elitebank");
        System.out.println("Username: sa");
        System.out.println("Password: (leave empty)");
        System.out.println("=============================\n");
        
        System.out.println("H2 Console is running. Press Ctrl+C to stop.");
        
        // Keep the application running
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("H2 Console stopped.");
        }
    }
}