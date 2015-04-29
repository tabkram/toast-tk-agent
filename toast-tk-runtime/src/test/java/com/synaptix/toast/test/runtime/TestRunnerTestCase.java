/**Copyright (c) 2013-2015, Synaptix Labs
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Creation date: 26 mars 2015
@author Sallah Kokaina <sallah.kokaina@gmail.com>

*/

package com.synaptix.toast.test.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.fest.assertions.AssertExtension;
import org.junit.AfterClass;
import org.junit.Test;

import com.synaptix.toast.core.IRepositorySetup;
import com.synaptix.toast.core.annotation.Check;
import com.synaptix.toast.test.runtime.mock.DefaultRepositorySetup;
import com.synpatix.toast.runtime.core.runtime.ToastRunnerHelper;
import com.synpatix.toast.runtime.core.runtime.ToastTestRunner;
import com.synpatix.toast.runtime.core.runtime.ToastTestRunner.FixtureExecCommandDescriptor;

public class TestRunnerTestCase {
	
	class Tata {
		
	}
	
	class Toto extends Tata{
		
	}
	
	class Titi{
		@Check("Titi")
		public void blabla(){
			
		}
	}
	
	
	@Test
	public void testEmptyResult() {
		ToastTestRunner runner = new ToastTestRunner(null, null);
		FixtureExecCommandDescriptor findMethodInClass = runner.findMethodInClass("Titi", Toto.class);
		assertNull(findMethodInClass);
	}
	
	@Test
	public void testNonEmptyResult() {
		ToastTestRunner runner = new ToastTestRunner(null, null);
		FixtureExecCommandDescriptor findMethodInClass = runner.findMethodInClass("Titi", Titi.class);
		assertNotNull(findMethodInClass);
	}
	
	@Test
	public void testArgumentBuild() {
		IRepositorySetup repo = new DefaultRepositorySetup();
		Map<String,Object> userVarMap = new HashMap<String, Object>();
		userVarMap.put("$variable", "200");
		repo.setUserVariables(userVarMap);
		Object buildArgument = ToastRunnerHelper.buildArgument(repo, "$variable");
		assertEquals(buildArgument, "200");
		buildArgument = ToastRunnerHelper.buildArgument(repo, "*$variable*");
		assertEquals(buildArgument, "200");
		buildArgument = ToastRunnerHelper.buildArgument(repo, "$vaiable");
		assertNull(buildArgument);
		buildArgument = ToastRunnerHelper.buildArgument(repo, "$$variable");
		assertEquals(buildArgument, "$variable");
		buildArgument = ToastRunnerHelper.buildArgument(repo, "*variable*");
		assertEquals(buildArgument, "variable");
	}
	
	
	@AfterClass
	public static void end(){
	}
}
