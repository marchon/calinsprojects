package test;

import ro.oneandone.bulk.Job;
import ro.oneandone.bulk.JobContext;

public class MyJob implements Job<MyInputPojo, MyOutputPojo> {

	@Override
	public void execute(JobContext<MyInputPojo, MyOutputPojo> ctx) {
		MyInputPojo input = ctx.getInput();
        MyOutputPojo output = new MyOutputPojo();
		
		sleep();
		
		System.out.println("Executing job with input: " + input + " in thread " + Thread.currentThread().getName());
        System.out.flush();
		
		sleep();
		
		int result = input.getP1() * 5;
        output.setResult(result);
		ctx.setOrUpdateOutput(output);

		sleep();
		
		String stringResult = input.getP2() + result;
        output.setResultString(stringResult);
        ctx.setOrUpdateOutput(output);
	}

	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}