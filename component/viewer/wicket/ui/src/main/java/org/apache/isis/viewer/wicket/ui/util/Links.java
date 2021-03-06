/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.viewer.wicket.ui.util;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public final class Links {

    public static <T extends Page> AbstractLink newSubmitLink(final String linkId, final PageParameters pageParameters, final Class<T> pageClass) {
        return new SubmitLink(linkId) {
            private static final long serialVersionUID = 1L;
            @Override
            public void onSubmit() {
                getForm().setResponsePage(pageClass, pageParameters);
                super.onSubmit();
            }
        };
    }

    public static <T extends Page> AbstractLink newAbstractLink(final String linkId, final PageParameters pageParameters, final Class<T> pageClass) {

      return new Link<T>(linkId) {
          private static final long serialVersionUID = 1L;
          @Override
          public void onClick() {
              this.setResponsePage(pageClass, pageParameters);
          }
          
      };
    }

    public static <T extends Page> AbstractLink newBookmarkablePageLink(final String linkId, final PageParameters pageParameters, final Class<T> pageClass) {
        return new BookmarkablePageLink<T>(linkId, pageClass, pageParameters);
    }

}
