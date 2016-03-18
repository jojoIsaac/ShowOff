/*
 * Copyright 2016 eneim@Eneim Labs, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package applications.apps.celsoft.com.showoff.Utilities.models;


import java.util.ArrayList;
import java.util.List;

import applications.apps.celsoft.com.showoff.Utilities.AppBackBoneClass;



/**
 * Created by eneim on 1/30/16.
 */
public class VideoSource {

  public static  String videoBaseUrl = AppBackBoneClass.parentUrL+"membersfiles/";



    public static List<String> VideoSOURCE= new ArrayList<>();
  public static final String[] SOURCES = {
          videoBaseUrl
  };
}
