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

import android.support.annotation.NonNull;

/**
 * Created by eneim on 1/30/16.
 */
public class SimpleVideoObject {

  public final String name = "VideoName";

  public final String video;

  public SimpleVideoObject(@NonNull String video) {
    this.video = video;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SimpleVideoObject that = (SimpleVideoObject) o;

    if (!name.equals(that.name)) return false;
    return video.equals(that.video);
  }

  @Override public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + video.hashCode();
    return result;
  }

  @Override public String toString() {
    return "SimpleVideoObject{" +
        "name='" + name + '\'' +
        ", video='" + video + '\'' +
        '}';
  }
}
