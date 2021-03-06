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

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class YaspFilters {

    private YaspFilters() {
        /* No instantiation */
    }

    public static final FilenameFilter TRAJECTORY_FILENAME_FILTER = (dir, name) -> {
        String filename = name.toUpperCase();
        return (filename.startsWith("TRAJ") || //
                filename.startsWith("ORBIT") ||  //
                filename.startsWith("DATASET-") || //
                filename.startsWith("RM."));
    };

    /**
     * the file-filter for yasp trajectory files
     */
    public static final FileFilter TRAJECTORY_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            } else {
                return TRAJECTORY_FILENAME_FILTER.accept(f.getParentFile(), f.getName());
            }
        }

        @Override
        public String getDescription() {
            return "Yasp data directory";
        }
    };
}
