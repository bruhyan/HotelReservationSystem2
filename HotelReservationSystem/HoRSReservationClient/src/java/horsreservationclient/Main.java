/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateless.CustomerControllerRemote;
import javax.ejb.EJB;

/**
 *
 * @author mdk12
 */
public class Main {

    @EJB
    private static CustomerControllerRemote customerControllerRemote;
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerControllerRemote);
        mainApp.runApp();
    }
    
}
