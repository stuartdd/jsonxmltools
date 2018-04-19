/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author stuartdd
 */
public class Functions {

    private boolean echoScriptOutput;
    private long poleForTime = 2000;
    private Map<String, List<String>> commands = new HashMap<>();

    public boolean isEchoScriptOutput() {
        return echoScriptOutput;
    }

    public void setEchoScriptOutput(boolean echoScriptOutput) {
        this.echoScriptOutput = echoScriptOutput;
    }

    public long getPoleForTime() {
        return poleForTime;
    }

    public void setPoleForTime(long poleForTime) {
        this.poleForTime = poleForTime;
    }

    public Map<String, List<String>> getCommands() {
        return commands;
    }

    public void setCommands(Map<String, List<String>> commands) {
        this.commands = commands;
    }


}
