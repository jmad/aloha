package cern.accsoft.steering.util.meas.data;

import cern.accsoft.steering.util.acc.BeamNumber;

public abstract class AbstractDataValue implements DataValue {

    private String name = null;
    private Plane plane;
    private BeamNumber beamNumber;

    @Override
    public final String toString() {
        return getKey();
    }

    @Override
    public final String getKey() {
        return ElementKeyUtil.composeKey(getName(), getPlane(), getBeam());
    }

    public final void setPlane(Plane plane) {
        this.plane = plane;
    }

    @Override
    public final Plane getPlane() {
        return plane;
    }

    public final void setName(String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    public final void setBeamNumber(BeamNumber beamNumber) {
        this.beamNumber = beamNumber;
    }

    @Override
    public final BeamNumber getBeam() {
        return beamNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((beamNumber == null) ? 0 : beamNumber.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((plane == null) ? 0 : plane.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractDataValue other = (AbstractDataValue) obj;
        if (beamNumber != other.beamNumber) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (plane != other.plane) {
            return false;
        }
        return true;
    }

}
