/**
 * 
 */
package cern.accsoft.steering.aloha.plugin;

import cern.accsoft.steering.aloha.plugin.api.AlohaPlugin;

import java.util.List;

/**
 * This is the interface of the class in aloha, which manages all the plugins.
 * 
 * @author kfuchsbe
 * 
 */
public interface PluginManager {

	/**
	 * @return a list of all available plugins
	 */
	public List<AlohaPlugin> getPlugins();

}
