package me.client.api.utils;

import me.client.api.Globals;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class Utils implements Globals {
    public static Vec3d getInterpolatedEntityPosition(Entity entity) {
        Vec3d a = entity.getPos();
        Vec3d b = new Vec3d(entity.prevX, entity.prevY, entity.prevZ);
        float p = mc.getTickDelta();
        return new Vec3d(MathHelper.lerp(p, b.x, a.x), MathHelper.lerp(p, b.y, a.y), MathHelper.lerp(p, b.z, a.z));
    }

    //ChatGPT lmao..
    public static Color hexRgbaToRgb(String hex) {
        String hexRgba = "#" + hex;
        if (hexRgba == null || hexRgba.isEmpty() || hexRgba.length() != 9 || !hexRgba.startsWith("#")) {
            throw new IllegalArgumentException("Invalid hexadecimal RGBA format.");
        }

        int red = Integer.parseInt(hexRgba.substring(1, 3), 16);
        int green = Integer.parseInt(hexRgba.substring(3, 5), 16);
        int blue = Integer.parseInt(hexRgba.substring(5, 7), 16);
        int alpha = Integer.parseInt(hexRgba.substring(7, 9), 16);

        return new Color(red, green, blue, alpha);
    }
}
