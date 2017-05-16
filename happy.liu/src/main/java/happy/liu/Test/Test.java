package happy.liu.Test;

import java.util.Scanner;

import happy.liu.dao.BaseActImpl;
import happy.liu.service.ServiceAct;

public class Test {

	public static void main(String[] args) {
		BaseActImpl act = new ServiceAct();
		/*act.createIndex();
		act.readIndex();
		act.queryIndex();*/
		act.readIndex();
		while(true){
			Scanner in = new Scanner(System.in);
			System.out.println("请输入出关键字：");
			String str = in.next();
			if("stop".equals(str)){
				break;
			}
			System.out.println("请输入查询的字段：id，name，email，content");
			String field = in.next();
			act.queryIndex(str, field);
		}
	}

}
