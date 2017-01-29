package uk.me.pilgrim.dev.discordBot;

import java.io.IOException;

import uk.me.pilgrim.dev.core.Core;


public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	Core.loadConfig();
    	Core.registerCoreModules();
    	
    	Core.register(new DiscordBot());
    	
    	Core.buildInjector();
    	Core.init();
    }
}
