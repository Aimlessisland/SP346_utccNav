package com.example.sp346_utccnav;

import java.util.ArrayList;
import java.util.List;

public class BuildingRepository {
    private static List<Building> buildings;
    private static List<Building> panolocate;

    static {
        buildings = new ArrayList<>();
        // Existing UTCC Buildings - Using null for startPixel
        buildings.add(new Building("ตึก 1", 13.77928170188982, 100.55997880652406, "อาคาร 1", R.drawable.b01, null));
        buildings.add(new Building("ตึก 3", 13.779200252232616, 100.56058307726535, "อาคาร 3", null, null));
        buildings.add(new Building("ตึก 5", 13.780841678457527, 100.56058472135702, "อาคาร 5", R.drawable.b05, null));
        buildings.add(new Building("ตึก 7", 13.779513125254471, 100.56108830609556, "อาคาร 7", R.drawable.b07, null));
        buildings.add(new Building("ตึก 8", 13.780415620893505, 100.56011212059003, "อาคาร จอดรถ", null, null));
        buildings.add(new Building("ตึก 10", 13.778748975450544, 100.5602919544518, "อาคาร 10", R.drawable.b10, null));
        buildings.add(new Building("ตึก 15", 13.779976965358271, 100.5599171278896, "อาคาร ประชุม", R.drawable.b15, null));
        buildings.add(new Building("ตึก 21", 13.780405340722664, 100.56100582817675, "อาคาร 21", R.drawable.b21, null));
        buildings.add(new Building("ตึก 23", 13.779824424180603, 100.56117346622875, "อาคาร 23", R.drawable.b23, null));
        buildings.add(new Building("ตึก 24", 13.780329795615001, 100.5604076955666, "อาคาร 24", R.drawable.b24, null));

        panolocate = new ArrayList<>();
        // Pre-set location for the call of navigate system with startPixel data
        panolocate.add(new Building("pos1", 13.779107732452406, 100.56008536126022, "", R.drawable.pos1, 1304));
        panolocate.add(new Building("pos2", 13.77951588735931, 100.5600943390289, "", R.drawable.pos2, 15802));
        panolocate.add(new Building("pos3", 13.779922920391614, 100.56011043228011, "", R.drawable.pos3, 15854));
        panolocate.add(new Building("pos4", 13.780113159249682, 100.56019349761799, "", R.drawable.pos4, 15811));
        panolocate.add(new Building("pos5", 13.780109178472406, 100.56022122769222, "", R.drawable.pos5, 16014));
        panolocate.add(new Building("pos6", 13.78008703591164, 100.5603902068517, "", R.drawable.pos6, 1328));
        panolocate.add(new Building("pos7", 13.780049263302141, 100.56054644552943, "", R.drawable.pos7, 15685));
        panolocate.add(new Building("pos8", 13.779833698811256, 100.56051895289362, "", R.drawable.pos8, 1333));
        panolocate.add(new Building("pos9", 13.779646789259028, 100.56051627068777, "", R.drawable.pos9, 15858));
        panolocate.add(new Building("pos10", 13.779478114656634, 100.56050218908725, "", R.drawable.pos10, 1362));
        panolocate.add(new Building("pos11", 13.779250827064768, 100.56048743693606, "", R.drawable.pos11, 1488));
        panolocate.add(new Building("pos12", 13.778949947450268, 100.56042641668063, "", R.drawable.pos12, 1357));
        panolocate.add(new Building("pos13", 13.778989022746252, 100.5602487203423, "", R.drawable.pos13, 15679));
        panolocate.add(new Building("pos14", 13.780201248851434, 100.56022133508323, "", R.drawable.pos14, 15954));
        panolocate.add(new Building("pos15", 13.78047885831314, 100.56027865724917, "", R.drawable.pos15, 1428));
        panolocate.add(new Building("pos16", 13.780624996915892, 100.56029927916715, "", R.drawable.pos16, 1452));
        panolocate.add(new Building("pos17", 13.780771814363218, 100.56030714344517, "", R.drawable.pos17, 1640));
        panolocate.add(new Building("pos18", 13.780755689908204, 100.56060598601032, "", R.drawable.pos18, 1488));
        panolocate.add(new Building("pos19", 13.780746903652219, 100.56079337874051, "", R.drawable.pos19, 16680));
        panolocate.add(new Building("pos20", 13.780564294107608, 100.56082192972997, "", R.drawable.pos20, 1371));
        panolocate.add(new Building("pos21", 13.78044488948589, 100.56076285298485, "", R.drawable.pos21, 1371));
        panolocate.add(new Building("pos22", 13.78044183096904, 100.56081040725486, "", R.drawable.pos22, 1388));
        panolocate.add(new Building("pos23", 13.780286139156539, 100.56077715652502, "", R.drawable.pos23, 1423));
        panolocate.add(new Building("pos24", 13.780101966906537, 100.56069486407233, "", R.drawable.pos24, 1417));
        panolocate.add(new Building("pos25", 13.779642936719387, 100.5607069428955, "", R.drawable.pos25, 1381));
        panolocate.add(new Building("pos26", 13.779343625476681, 100.56071172911335, "", R.drawable.pos26, 15596));
        panolocate.add(new Building("pos27", 13.77920737751155, 100.56082134828925, "", R.drawable.pos27, 16045));
        panolocate.add(new Building("pos28", 13.779146705802896, 100.561012654609, "", R.drawable.pos28, 1257));
        panolocate.add(new Building("pos29", 13.779125386078636, 100.56125081860645, "", R.drawable.pos29, 1185));
        panolocate.add(new Building("pos30", 13.778938488155815, 100.56001895355652, "", R.drawable.pos30, 1390));
        panolocate.add(new Building("pos31", 13.77874745759615, 100.55986199908156, "", R.drawable.pos31, 1390));
    }

    public static List<Building> getBuildings() { return buildings; }
    public static List<Building> getPanolocate() { return panolocate; }
}