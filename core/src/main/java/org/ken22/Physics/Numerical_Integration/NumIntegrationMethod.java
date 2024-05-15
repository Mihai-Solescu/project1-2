package org.ken22.Physics.Numerical_Integration;

import net.objecthunter.exp4j.Expression;
import org.ken22.Physics.Numerical_Derivation.NumDerivationMethod;
import org.ken22.Physics.Vectors.GVec4;
import org.ken22.interfaces.IFunc;

import java.util.ArrayList;

public interface NumIntegrationMethod {

    public GVec4 execute(ArrayList<GVec4> stateVectors, double timeStep, IFunc<Double, Double> funcx, IFunc<Double, Double> funcy, Expression terrain, NumDerivationMethod differentiator);

}
