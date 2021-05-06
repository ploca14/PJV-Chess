package cz.cvut.fel.pjv.model.chestpieces;

public enum Color {
    WHITE() {
        @Override
        public javafx.scene.paint.Color getPaintColor() {
            return javafx.scene.paint.Color.WHITE;
        }
    },
    BLACK() {
        @Override
        public javafx.scene.paint.Color getPaintColor() {
            return javafx.scene.paint.Color.BLACK;
        }
    },
    LIGHT() {
        @Override
        public javafx.scene.paint.Color getPaintColor() {
            return javafx.scene.paint.Color.BEIGE;
        }
    },
    DARK() {
        @Override
        public javafx.scene.paint.Color getPaintColor() {
            return javafx.scene.paint.Color.TAN;
        }
    };

    public abstract javafx.scene.paint.Color getPaintColor();
}
