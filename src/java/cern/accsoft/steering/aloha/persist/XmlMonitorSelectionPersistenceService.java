package cern.accsoft.steering.aloha.persist;

import cern.accsoft.steering.aloha.conf.MonitorSelection;
import cern.accsoft.steering.jmad.util.xml.XmlXStreamService;

import com.thoughtworks.xstream.XStream;

/**
 * allows to save and load monitor-selections.
 * 
 * @author kaifox
 */
public class XmlMonitorSelectionPersistenceService extends XmlXStreamService<MonitorSelection> {

    /** the extension for the xml file */
    public static final String XML_FILE_EXTENSION = ".ams.xml";

    @Override
    public String getFileExtension() {
        return XML_FILE_EXTENSION;
    }

    @Override
    protected void initializeXStream(XStream xstream) {
        /* process the annotations of all classes we need */
        xstream.autodetectAnnotations(true);
        Class<?>[] classes = new Class<?>[] { MonitorSelection.class };
        xstream.processAnnotations(classes);
    }

    @Override
    protected Class<? extends MonitorSelection> getSaveableClass() {
        return MonitorSelection.class;
    }

}
