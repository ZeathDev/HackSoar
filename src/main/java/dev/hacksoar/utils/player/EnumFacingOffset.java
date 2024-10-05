package dev.hacksoar.utils.player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

/**
 * @author: Liycxc
 * @date: 2023-06-30
 * @time: 19:34
 */
@Setter
@Getter
public class EnumFacingOffset {
    private final Vec3 offset;
    public EnumFacing enumFacing;

    public EnumFacingOffset(final EnumFacing enumFacing, final Vec3 offset) {
        this.enumFacing = enumFacing;
        this.offset = offset;
    }
}
