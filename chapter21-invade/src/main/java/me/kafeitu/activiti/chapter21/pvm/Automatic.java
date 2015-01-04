/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.kafeitu.activiti.chapter21.pvm;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

import java.util.List;


/**
 * 自动执行的活动
 * @author Tom Baeyens
 */
public class Automatic implements ActivityBehavior {
    public void execute(ActivityExecution execution) throws Exception {
        List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
        if (outgoingTransitions.isEmpty()) {
            execution.end();
            System.out.println("流程已结束，结束节点：" + execution.getActivity().getId());
        } else {
            System.out.println("自动节点：" + execution.getActivity().getId());
            execution.take(outgoingTransitions.get(0));
        }
    }
}
