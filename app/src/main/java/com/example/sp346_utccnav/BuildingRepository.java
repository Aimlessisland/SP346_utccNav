package com.example.sp346_utccnav;

import java.util.ArrayList;
import java.util.List;

public class BuildingRepository {
    private static List<Building> buildings;
    private static List<Building> panolocate;

    static {
        buildings = new ArrayList<>();
        // Existing UTCC Buildings
        buildings.add(new Building("ตึก 1", 13.77928170188982, 100.55997880652406, "อาคาร 1", R.drawable.b01));
        buildings.add(new Building("ตึก 3", 13.779200252232616, 100.56058307726535, "อาคาร 3", null));
        buildings.add(new Building("ตึก 5", 13.780841678457527, 100.56058472135702, "อาคาร 5", R.drawable.b05));
        buildings.add(new Building("ตึก 7", 13.779513125254471, 100.56108830609556, "อาคาร 7", R.drawable.b07));
        buildings.add(new Building("ตึก 8", 13.780415620893505, 100.56011212059003, "อาคาร จอดรถ", null));
        buildings.add(new Building("ตึก 10", 13.778748975450544, 100.5602919544518, "อาคาร 10", R.drawable.b10));
        buildings.add(new Building("ตึก 15", 13.779976965358271, 100.5599171278896, "อาคาร ประชุม", R.drawable.b15));
        buildings.add(new Building("ตึก 21", 13.780405340722664, 100.56100582817675, "อาคาร 21", R.drawable.b21));
        buildings.add(new Building("ตึก 23", 13.779824424180603, 100.56117346622875, "อาคาร 23", R.drawable.b23));
        buildings.add(new Building("ตึก 24", 13.780329795615001, 100.5604076955666, "อาคาร 24", R.drawable.b24));

        panolocate = new ArrayList<>();
        // Pre-set location for the call of navigate system
        panolocate.add(new Building("pos1", 13.779107732452406, 100.56008536126022, "", R.drawable.pos1));
        panolocate.add(new Building("pos2", 13.77951588735931, 100.5600943390289, "", R.drawable.pos2));
        panolocate.add(new Building("pos3", 13.779922920391614, 100.56011043228011, "", R.drawable.pos3));
        panolocate.add(new Building("pos4", 13.780113159249682, 100.56019349761799, "", R.drawable.pos4));
        panolocate.add(new Building("pos5", 13.780109178472406, 100.56022122769222, "", R.drawable.pos5));
        panolocate.add(new Building("pos6", 13.78008703591164, 100.5603902068517, "", R.drawable.pos6));
        panolocate.add(new Building("pos7", 13.780049263302141, 100.56054644552943, "", R.drawable.pos7));
        panolocate.add(new Building("pos8", 13.779833698811256, 100.56051895289362, "", R.drawable.pos8));
        panolocate.add(new Building("pos9", 13.779646789259028, 100.56051627068777, "", R.drawable.pos9));
        panolocate.add(new Building("pos10", 13.779478114656634, 100.56050218908725, "", R.drawable.pos10));
        panolocate.add(new Building("pos11", 13.779250827064768, 100.56048743693606, "", R.drawable.pos11));
        panolocate.add(new Building("pos12", 13.778949947450268, 100.56042641668063, "", R.drawable.pos12));
        panolocate.add(new Building("pos13", 13.778989022746252, 100.5602487203423, "", R.drawable.pos13));
    }

    public static List<Building> getBuildings() { return buildings; }
    public static List<Building> getPanolocate() { return panolocate; }
}