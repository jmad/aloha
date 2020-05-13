package cern.accsoft.steering.aloha;

import cern.accsoft.steering.jmad.gui.JMadGui;
import cern.accsoft.steering.jmad.gui.config.JMadGuiStandaloneConfiguration;
import cern.accsoft.steering.jmad.gui.manage.JMadGuiPreferences;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@Import({ JMadGuiStandaloneConfiguration.class })
@ImportResource("cern/accsoft/steering/aloha/app-ctx-aloha.xml")
public class AlohaSpringConfiguration {

    @EventListener
    public void setJMadPrefGui(ContextRefreshedEvent event) {
        JMadGuiPreferences pref = event.getApplicationContext().getBean(JMadGui.class).getJmadGuiPreferences();
        pref.setCleanupOnClose(false);
        pref.setExitOnClose(false);
        pref.setMainFrame(false);
        pref.setEnabledChangeModel(false);
    }

}
