package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

public class CalcDB {


	public static String calcularDB(File file) throws IOException {
		
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(4);
		
		FileInputStream inFile = new FileInputStream(file);
		InputStreamReader in = new InputStreamReader(inFile);
		Instances base = new Instances(in);
		base.setClassIndex(base.numAttributes() - 1);
		Mensurably<Instance> difference = new Difference();
		String saida = new String(df.format(daviesBouldin(base, difference)));
		return saida;
		
	}

 	private static double daviesBouldin(Instances base, Mensurably<Instance> difference) {
		Instances[] group = new Instances[base.numClasses()];
		for (int i = 0; i < group.length; i++) {
			group[i] = new Instances(base, 0);
		}

		for (int i = 0; i < base.numInstances(); i++) {
			group[(int) base.instance(i).classValue()].add(base.instance(i));
		}

		double[] E = new double[group.length];
		Instance[] center = new Instance[group.length];
		for (int i = 0; i < group.length; i++) {
			center[i] = center(group[i]);
			center[i].setDataset(group[i]);
			E[i] = medianSquaredDistance(group[i], center[i], difference);
		}

		double db = 0;
		for (int i = 0; i < group.length; i++) {
			db += mrs(i, E, center, difference);
		}
		return db / (double) group.length;
	}

	protected static double mrs(int index, double[] E, Instance[] center, Mensurably<Instance> groupDistance) {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < center.length; i++) {
			if (i != index) {
				double aux = rs(E[index], E[i], center[index], center[i], groupDistance);
				if (aux > max) {
					max = aux;
				}
			}
		}
		return max;
	}

	protected static double rs(double E1, double E2, Instance center1, Instance center2, Mensurably<Instance> groupDistance) {
		return (E1 + E2) / (groupDistance.distance(center1, center2));
	}

	protected static double medianSquaredDistance(Instances base, Instance center, Mensurably<Instance> difference) {
		double median = 0;
		for (int i = 0; i < base.numInstances(); i++)
			median += Math.pow(difference.distance(base.instance(i), center), 2);
		return median / (double) base.numInstances();
	}

	protected static Instance center(Instances base) {
		Instance center = new SparseInstance(base.firstInstance());
		for (int i = 0; i < base.numAttributes(); i++) {
			if (base.attribute(i).isNominal())
				center.setValue(i, moda(base, base.attribute(i)));
			else if (base.attribute(i).isNumeric())
				center.setValue(i, median(base, base.attribute(i)));
			else
				throw new IllegalArgumentException(
						"Attribute " + base.attribute(i).name() + " not is numeric or nominal");
		}
		return center;
	}

	protected static double moda(Instances base, Attribute att) {
		double[] count = new double[att.numValues()];
		for (int i = 0; i < base.numInstances(); i++)
			count[(int) base.instance(i).value(att)]++;
		return maxIndex(count);
	}

	protected static double median(Instances base, Attribute att) {
		double median = 0;
		for (int i = 0; i < base.numInstances(); i++)
			median += base.instance(i).value(att);
		return median / (double) base.numInstances();
	}

	private static int maxIndex(double... values) {
		int max = 0;
		for (int i = 1; i < values.length; i++)
			if (values[i] > values[max])
				max = i;
		return max;
	}

}
