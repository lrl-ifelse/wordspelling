package org.ifelse.vl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *
 * 连接条件
 */
public class Line {

	public FlowPoint parent;
	public FlowPoint child;


	public String id;
	public String fromid;
	public String toid;

	public String conditions;
	public boolean isNumber;



	public void setParent(FlowPoint parent){

		this.parent = parent;
		if( parent != null )
			parent.addChildLine(this);


	}

	public boolean isRight(FlowBox box){

		if( conditions == null || conditions.length() == 0 || "else".equals(conditions) )
			return true;


		String[] strs = new String[2]; //conditions.split(">=|<=|==|>|<");

		Matcher matcher =  null;
		try {
			Pattern pattern_field = Pattern.compile(">=|<=|==|<>|>|<");
			matcher = pattern_field.matcher(conditions);
		}catch (Exception e){
			return false;
		}

		if( matcher.find() )
		{
			int start = matcher.start();
			int end = matcher.end();
			strs[0] = conditions.substring(0,start);
			strs[1] = conditions.substring(end);


			String key = conditions.substring(start,end);

			String left = box.getVarString(strs[0]);
			String right = box.getVarString(strs[1]);


			box.log("%s[%s] %s %s[%s]",strs[0],left ,key,strs[1],right);


			if( left == null || right == null ) {

				if( key.equals("<>") )
					return true;
				return false;
			}

			if( !isNumber ) {
				switch (key) {

					case ">=":
						return left.compareTo(right) >= 0;

					case "<=":
						return left.compareTo(right) <= 0;

					case "==":
						return left.compareTo(right) == 0;

					case ">":
						return left.compareTo(right) > 0;

					case "<":
						return left.compareTo(right) < 0;


					case "<>":
						return !left.equals(right);

				}
			}
			else
			{


				long n_left = Long.parseLong(left);
				long n_right = Long.parseLong(right);

				switch (key) {

					case ">=":
						return n_left>=n_right;

					case "<=":
						return n_left<=n_right;

					case "==":
						return n_left == n_right;

					case ">":
						return n_left>n_right;

					case "<":
						return n_left<n_right;


					case "<>":
						return n_left != n_right;

				}


			}




		}
		else if( box.isVar(conditions) ){

			//if( box.getValue(conditions) != null )


			Object obj = box.getValue(conditions);

			boolean result =  ( obj != null && !"false".equals(obj.toString()) && !"0".equals(obj.toString()) );

			box.log("[%s] is OK :%b",conditions,result);

			return result;


		}


		//box.log("line :%s is false ",conditions);

		return false;








	}


	public void setChild(FlowPoint point) {
		child = point;
	}
}
