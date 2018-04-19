/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.button.tests;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.testutil.AbstractComponentIT;
import com.vaadin.flow.testutil.TestPath;

@TestPath("template-button")
public class TemplateButtonIT extends AbstractComponentIT {

    @Test
    public void setText_overridesAllContent() {
        open();

        WebElement template = findElement(By.id("button-template"));

        WebElement button = findInShadowRoot(template, By.id("button")).get(0);
        Assert.assertTrue("Button should have displayed", button.isDisplayed());
        Assert.assertEquals("Button should contain caption", "Template caption",
                button.getText());

        button.click();

        button = findInShadowRoot(template, By.id("button")).get(0);

        Assert.assertEquals("Button caption should only be the server side caption", "clicked",
                button.getText());
    }
}
