// Copyright 2010 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.stardroid.data;

import com.google.android.stardroid.base.Closeables;
import com.google.android.stardroid.source.AstronomicalSource;
import com.google.android.stardroid.source.proto.SourceFullProto.AstronomicalSourceProto;
import com.google.android.stardroid.source.proto.SourceFullProto.AstronomicalSourcesProto;
import com.google.android.stardroid.source.proto.SourceFullProto.GeocentricCoordinatesProto;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Base class for converting (ASCII) text data files into protocol buffers.
 * This class writes out both the binary and human readable version of the
 * messages.
 *
 * @author Brent Bryan
 */
public abstract class AbstractProtoWriter {
  /**
   * Returns the AstronomicalSource associated with the given line, or null if
   * the line does not correspond to a valid {@link AstronomicalSource}.
   */
  protected abstract AstronomicalSourceProto getSourceFromLine(String line, int index);

  protected GeocentricCoordinatesProto getCoords(float ra, float dec) {
    return GeocentricCoordinatesProto.newBuilder()
        .setRightAscension(ra)
        .setDeclination(dec)
        .build();
  }

  public AstronomicalSourcesProto readSources(BufferedReader in) throws IOException {
    AstronomicalSourcesProto.Builder builder = AstronomicalSourcesProto.newBuilder();

    String line;
    while ((line = in.readLine()) != null) {
      line = line.trim();
      if (line.equals("")) {
        continue;
      }

      AstronomicalSourceProto source = getSourceFromLine(line, builder.getSourceCount());

      if (source != null) {
        builder.addSource(source);
      }
    }

    return builder.build();
  }

  public void writeFiles(String prefix, AstronomicalSourcesProto sources) throws IOException {

    FileOutputStream out = null;
    try {
      out = new FileOutputStream(prefix + ".binary");
      sources.writeTo(out);
    } finally {
      Closeables.closeSilently(out);
    }

    PrintWriter writer = null;
    try {
      writer = new PrintWriter(new FileWriter(prefix + ".ascii"));
      writer.append(sources.toString());
    } finally {
      Closeables.closeSilently(out);
    }

    System.out.println("Successfully wrote " + sources.getSourceCount() + " sources.");
  }

  public void run(String[] args) throws IOException {
    if (args.length != 2) {
      System.out.printf("Usage: %s <inputfile> <outputprefix>", this.getClass().getCanonicalName());
      System.exit(1);
    }
    args[0] = args[0].trim();
    args[1] = args[1].trim();

    System.out.println("Input File: "+args[0]);
    System.out.println("Output Prefix: "+args[1]);

    writeFiles(args[1], readSources(new BufferedReader(new FileReader(args[0]))));
  }
}
