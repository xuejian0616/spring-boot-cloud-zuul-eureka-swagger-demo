package cn.xuhao.demo.paymentserver;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentServerApplicationTests {

	@Test
	public void contextLoads() {
		long sumtime = 0;
		int count = 10000;
		//循环调用，请求id连续，判断请求是否丢失
		for(int i= 0;i< count;i++) {
			try {
				long start = System.currentTimeMillis();
				//jsoup 调用消费者地址
				Document doc = Jsoup.connect("http://localhost:8082/payForSomething/"+i).get();
				Elements elements = doc.select("body");  //获取body中的内容
				long end = System.currentTimeMillis();
				long time = end-start;  //记录调用时间
				sumtime += time;
				String result = "执行时间" + time + "\t\t"+elements.text();
				System.out.println(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//计算平均调用时间
		float avgTime = sumtime/count;
		System.out.println("平均执行时间：" + avgTime +"毫秒");

	}

}
