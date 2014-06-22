/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.setblack.chatsample.web;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jratajsk
 */
@XmlRootElement
public class IncomingMessage {
    public String nick;
    public String content;
}
