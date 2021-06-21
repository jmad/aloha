package cern.accsoft.steering.util.meas.yasp.browse;

import static cern.accsoft.steering.util.acc.BeamNumber.BEAM_1;
import static cern.accsoft.steering.util.acc.BeamNumber.BEAM_2;
import static cern.accsoft.steering.util.acc.BeamNumber.BEAM_3;
import static cern.accsoft.steering.util.acc.BeamNumber.BEAM_4;

import java.awt.*;
import java.util.Map;

import cern.accsoft.steering.util.acc.BeamNumber;
import com.google.common.collect.ImmutableMap;

public final class YaspColors {
    private YaspColors() {
        throw new UnsupportedOperationException("static only");
    }

    public static final Map<BeamNumber, Color> BEAM_COLOR = ImmutableMap.<BeamNumber, Color>builder()
            .put(BEAM_1, new Color(204, 220, 255)) //
            .put(BEAM_2, new Color(255, 240, 200)) //
            .put(BEAM_3, new Color(255, 205, 205)) //
            .put(BEAM_4, new Color(155, 255, 155)) //
            .build();
}
