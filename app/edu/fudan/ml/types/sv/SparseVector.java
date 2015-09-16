package edu.fudan.ml.types.sv;

import java.util.ArrayList;

/**
 * 稀疏向量，并实现各种向量运算
 * @version 1.0
 * @since 1.0
 * 
 */
public interface SparseVector{


	/**
	 * 向量减法: x-y
	 * 
	 * @param sv
	 */
	public void minus(SparseVector sv);

	/**
	 * 对应位置加上值: x[i] = x[i]+c
	 * 
	 * @param id
	 * @param c
	 */
	public void add(int id, float c);

	/**
	 * 向量加法：x+y
	 * 
	 * @param sv
	 */
	public void plus(SparseVector sv);

	/**
	 * 计算x+y*w
	 * 
	 * @param sv 
	 * @param w
	 */
	public void plus(SparseVector sv, float w);

	public float elementAt(int id);

	public int[] indices();
	
	public float dotProduct(float[] v);

	/**
	 * 向量点积: x*y
	 * 
	 * @param sv
	 * @return 结果
	 */
	public float dotProduct(SparseVector sv);

	/**
	 * 向量点积: x*(y+c)
	 * 
	 * @param sv
	 * @return 结果
	 */
	public float dotProduct(SparseVector sv, float c);


	public void scaleMultiply(float c);

	public void scaleDivide(float c);

	public float l1Norm();

	public float l2Norm2();

	public float l2Norm();

	public float infinityNorm();

	public SparseVector replicate(ArrayList<Integer> list, int dim);

	/**
	 * 计算两个向量距离
	 * 
	 * @param sv
	 * @return 距离值
	 */
	public float euclideanDistance(SparseVector sv);

	public void clear();

	public void normalize();

	public void normalize2();
	public float get(int idx);

	public void put(int idx, float value);
	/**
	 * 去掉第idx维特征
	 * @param idx
	 * @return
	 */
	public float remove(int idx);

	/**
	 * 稀疏元素个数
	 * @return
	 */
	public int size();
	
	public boolean containsKey(int idx);
	
}
