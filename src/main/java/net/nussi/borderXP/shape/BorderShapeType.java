package net.nussi.borderXP.shape;

public enum BorderShapeType {
    SPHERE(new BorderSphere());

    private BorderShape borderShape;

    BorderShapeType(BorderShape borderShape) {
        this.borderShape = borderShape;
    }

    public BorderShape getShape() {
        return borderShape;
    }

}
