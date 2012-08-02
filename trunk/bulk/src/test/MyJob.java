package test;

import ro.oneandone.bulk.Job;
import ro.oneandone.bulk.JobContext;

public class MyJob implements Job<MyInputPojo> {

	private static final String RESULT_STRING = "resultString";
	private static final String RESULT = "result";

	@Override
	public void execute(JobContext<MyInputPojo> ctx) {
		MyInputPojo input = ctx.getInput();
		
		sleep();
		
		System.out.println(input);
		
		sleep();
		
		int result  = input.getP1() * 5;
		ctx.putOutputAttribute(RESULT, result);

		sleep();
		
		String stringResult = input.getP2() + result;
		ctx.putOutputAttribute(RESULT_STRING, stringResult);
	}

	private void sleep() {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}