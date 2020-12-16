package db;

import weka.core.Instance;

public class Difference implements Mensurably<Instance> {

	public double distance(Instance a, Instance b) {
		double diff = 0;
		for (int i = 1; i < a.numAttributes() - 1; i++) {
			if (a.attribute(i).isNominal()) {
				// testa se atributo é hieráquico
				if (a.stringValue(i).startsWith("R.")) {
					diff += computeDistance(a.stringValue(i), b.stringValue(i));
				}
				// atributo nao hieraquico
				else {
					diff += a.value(i) == b.value(i) ? 0 : 1;
				}
			}
			// atributo numerico
			else {
				diff += Math.abs(a.value(i) - b.value(i));
				// diff += Math.sqrt(Math.pow(a.value( i ), 2) - Math.pow(b.value( i ), 2));
			}
		}
		return diff;
	}
	
	private double computeDistance(String s1, String s2) {
		String[] t1 = s1.split("[.]");
		String[] t2 = s2.split("[.]");

		if (s1.compareTo(s2) == 0) {
			return 0;
		} else {
			if (t1[1].compareToIgnoreCase(t2[1]) == 0) {
				if (t1[2].compareToIgnoreCase(t2[2]) == 0) {
					if (t1[3].compareToIgnoreCase(t2[3]) == 0) {
						if (t1[4].compareToIgnoreCase(t2[4]) == 0) {
							if (t1[5].compareToIgnoreCase(t2[5]) == 0) {
								if (t1[6].compareToIgnoreCase(t2[6]) == 0) {
									return 0.015625;
								} else {
									return 0.03125;
								}
							} else {
								return 0.0625;
							}
						} else {
							return 0.125;
						}
					} else {
						return 0.25;
					}
				} else {
					return 0.5;
				}
			} else {
				return 1;
			}
		}
	}

}
