package me.albert.amcrmb.utils.map;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

public class PayRenderer extends MapRenderer {
    private BufferedImage image;

    public PayRenderer(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        mapCanvas.drawImage(0, 0, image);
    }

}
