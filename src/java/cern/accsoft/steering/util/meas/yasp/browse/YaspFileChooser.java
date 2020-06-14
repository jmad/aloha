// @formatter:off
 /*******************************************************************************
 *
 * This file is part of JMad.
 * 
 * Copyright (c) 2008-2011, CERN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ******************************************************************************/
// @formatter:on

package cern.accsoft.steering.util.meas.yasp.browse;

import javax.swing.*;
import java.io.File;

/**
 * A file chooser, which contains a preview for yasp files
 *
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class YaspFileChooser extends JFileChooser {
    private YaspPreviewPanel dvPanel = new YaspPreviewPanel();

    public YaspFileChooser() {
        super.addChoosableFileFilter(YaspFilters.TRAJECTORY_FILE_FILTER);
        super.setAcceptAllFileFilterUsed(true);
        super.setAccessory(this.dvPanel);
        super.addPropertyChangeListener(evt -> {
            String propertyName = evt.getPropertyName();

            /* Make sure we are responding to the right event. */
            if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                File selectedFile = (File) evt.getNewValue();

                if (selectedFile == null) {
                    /* no file selected */
                    return;
                }
                dvPanel.setYaspFile(selectedFile);
            }
        });
    }

}
