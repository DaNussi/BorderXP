package net.nussi.borderXP.shape;

public enum BorderShapeType {
    SPHERE(new BorderSphere()),
    CYLINDER(new BorderCylinder()),
    CUBE(new BorderCube());

    private BorderShape borderShape;

    BorderShapeType(BorderShape borderShape) {
        this.borderShape = borderShape;
    }

    public BorderShape getShape() {
        return borderShape;
    }

}
