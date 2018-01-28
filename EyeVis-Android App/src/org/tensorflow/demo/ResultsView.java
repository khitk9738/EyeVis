package org.tensorflow.demo;

import org.tensorflow.demo.Classifier.Recognition;

import java.util.List;

public interface ResultsView {
  public void setResults(final List<Recognition> results);
}
