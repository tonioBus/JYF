/**
 * 
 */
package com.justyour.food.server;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Queue;

import javax.xml.bind.JAXBException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Test;

import com.justyour.food.shared.tools.ToolsShared.LimitedQueue;
import com.justyour.food.shared.tools.ToolsShared.Line;

/**
 * @author tonio
 * 
 */
public class ToolsServerTest {

	class ExecStreamHandler extends LogOutputStream {
		@Override
		protected void processLine(String line, int level) {
			System.out.println("["+level+"]:"+line);
		}		
	}
	
	@Test
	public void testExec2() throws ExecuteException, IOException, JAXBException {
		Queue<Line> queue = new LimitedQueue<>(10000);
		ToolsServer.execWget("http://recettes.doctissimo.fr/", queue);
	}
	
	@Test
	public void testExec() throws JAXBException, ExecuteException, IOException {
		Parameters param = Parameters.getParameters(JYFServletContext.getParamFile());
		DefaultExecutor exec = new DefaultExecutor();
		
		exec.setWorkingDirectory(new File(param.getWorkingTmp()));
		PumpStreamHandler psh = new PumpStreamHandler(new ExecStreamHandler(), new ExecStreamHandler());
		exec.setStreamHandler(psh);
		String commandSz = param.getWget();
		CommandLine cl = new CommandLine(commandSz);
		String[] options = param.getWgetOptions().split("\\s+");
		cl.addArguments(options);
		cl.addArguments("http://recettes.doctissimo.fr/");		
		//+" test");// +" "+param.getWgetOptions()+" http://recettes.doctissimo.fr/index.html");
		System.out.println("command ["+cl.getExecutable()+"]");
		int exitvalue = exec.execute(cl);
		assertTrue(exitvalue == 1);
	}

}
