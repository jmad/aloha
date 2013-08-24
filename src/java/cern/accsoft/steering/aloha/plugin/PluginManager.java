/**
 * 
 */
package cern.accsoft.steering.aloha.plugin;

import java.util.List;

import cern.accsoft.steering.aloha.plugin.api.AlohaPlugin;

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
