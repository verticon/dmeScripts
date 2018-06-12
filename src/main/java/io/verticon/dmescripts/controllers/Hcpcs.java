package io.verticon.dmescripts.controllers;

public class Hcpcs {
	public static String description(String hcpc) {
		switch (hcpc) {
		// Indwelling
		case "A4338": return "Latex";
		case "A4340": return "Specialty";
		case "A4344": return "All Silicone";

		// Condom
		case "A4349": return "Latex";

		// Intermittent
		case "A4351": return "Straight";
		case "A4352": return "Coude' Tip";

		default: return String.format("%s - unrecognised HCPC", hcpc);
		}
	}
}
