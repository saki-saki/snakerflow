/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.snaker.engine.handlers.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.snaker.engine.model.*;

/**
 * 合并分支操作的处理器
 * 
 * @author yuqs
 * @since 1.0
 */
public class MergeBranchHandler extends AbstractMergeHandler {
	private JoinModel model;

	public MergeBranchHandler(JoinModel model) {
		this.model = model;
	}

	/**
	 * 对join节点的所有输入变迁进行递归，查找join至fork节点的所有中间task元素
	 * 
	 * @param node
	 * @param taskNames
	 */
	private void findForkTaskNames(NodeModel node, Set<String> taskNames) {
		if (node instanceof ForkModel) {
			return;
		}
		List<TransitionModel> inputs = node.getInputs();
		for (TransitionModel tm : inputs) {
			NodeModel source = tm.getSource();
			String name = source.getName();
			if (!taskNames.contains(name)) {
				if (source instanceof WorkModel) {
					taskNames.add(name);
				}
				findForkTaskNames(source, taskNames);
			}
		}
	}

	/**
	 * 对join节点的所有输入变迁进行递归，查找join至fork节点的所有中间task元素
	 * 
	 * @see org.snaker.engine.handlers.impl.AbstractMergeHandler#findActiveNodes()
	 */
	protected String[] findActiveNodes() {
		Set<String> taskNames = new HashSet<String>();
		findForkTaskNames(model, taskNames);
		return taskNames.toArray(new String[] {});
	}
}
