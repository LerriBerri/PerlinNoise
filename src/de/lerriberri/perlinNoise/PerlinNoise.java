package de.lerriberri.perlinNoise;

import java.util.Random;

public class PerlinNoise {

    private final static Vector upLeft = new Vector(-1, -1);
    private final static Vector upRight = new Vector(1, -1);
    private final static Vector downRight = new Vector(1, 1);
    private final static Vector downLeft = new Vector(-1, 1);

    private record Vector(double x, double y) { }

    private final int[][] map;

    public PerlinNoise(long seed) {
        Random random = new Random(seed);
        map = new int[255][255];

        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                map[i][j] = random.nextInt(4);
            }
        }
    }

    public PerlinNoise() {
        Random random = new Random();
        map = new int[255][255];

        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                map[i][j] = random.nextInt(4);
            }
        }
    }

    public double getValue(double x, double y) {
        int xFloor = (int) Math.floor(x) % 255;
        int yFloor = (int) Math.floor(y) % 255;

        double xFloat = x - Math.floor(x);
        double yFloat = y - Math.floor(y);

        Vector distUpLeft = new Vector(      xFloat    ,   yFloat       );
        Vector distUpRight = new Vector(  xFloat - 1,   yFloat       );
        Vector distDownRight = new Vector(xFloat - 1,yFloat - 1   );
        Vector distDownLeft = new Vector(    xFloat    ,yFloat - 1   );

        double dotProductUpLeft = dotProduct(distUpLeft      , getVector(map[(xFloor    )      ][(yFloor    )      ]));
        double dotProductUpRight = dotProduct(distUpRight    , getVector(map[(xFloor + 1) % 255][(yFloor    )      ]));
        double dotProductDownRight = dotProduct(distDownRight, getVector(map[(xFloor + 1) % 255][(yFloor + 1) % 255]));
        double dotProductDownLeft = dotProduct(distDownLeft  , getVector(map[(xFloor    )      ][(yFloor + 1) % 255]));

        double u = fade(xFloat);
        double v = fade(yFloat);

        return lerp(
                lerp(dotProductUpLeft, dotProductDownLeft, v),
                lerp(dotProductUpRight, dotProductDownRight, v),
                u
        );
    }

    private double dotProduct(Vector vec1, Vector vec2) {
        return (vec1.x * vec2.x) + (vec1.y * vec2.y);
    }

    private Vector getVector(int id) {
        return switch (id) {
            case 0 -> upLeft;
            case 1 -> upRight;
            case 2 -> downRight;
            case 3 -> downLeft;
            default -> null;
        };
    }

    private double lerp(double x1, double x2, double t) {
        return x1 + t * (x2 - x1);
    }

    private double fade(double t) {
        return ((6*t - 15)*t + 10)*t*t*t;
    }

}
