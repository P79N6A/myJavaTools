package com.caucho.tools.profiler;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

public class ProfilerWriter {
	//private static final PrintStream out = System.out;

	//private final ProfilerManager profilerManager = ProfilerManager.getLocal();

	public void writeHtml(String sort) {
		/*
		ProfilerNodeComparator comparator;

		if ("count".equals(sort))
			comparator = new CountComparator();
		else
			comparator = new TimeComparator();

		comparator.setDescending(true);

		out.println("Profiling Results");
		out
				.println("Name\tAverage Time\tMin Time\tMax Time\tTotal Time\tInvocation Count");

		ProfilerPoint root = profilerManager.getRoot();

		List<ProfilerPoint> children = root.getChildren();
		Collections.sort(children, comparator);
		for (ProfilerPoint child : children)
			display(child, comparator, 0);
			*/
			
	}
/*
	private void display(ProfilerPoint node, ProfilerNodeComparator comparator,
			int depth) {

		if (node == null)
			return;

		List<ProfilerPoint> children = node.getChildren();
		Collections.sort(children, comparator);

		long thisTime = node.getTime();
		long minTime = node.getMinTime();
		long maxTime = node.getMaxTime();

		long childrenTime = 0;

		for (ProfilerPoint child : children) {
			childrenTime += child.getTime();
		}

		long totalTime = childrenTime + thisTime;

		long invocationCount = node.getInvocationCount();
		long averageThisTime;
		long averageTotalTime;
		long averageChildrenTime;

		if (invocationCount <= 0) {
			averageThisTime = -1;
			averageTotalTime = -1;
			averageChildrenTime = -1;
		} else {
			averageThisTime = thisTime / invocationCount;
			averageTotalTime = totalTime / invocationCount;
			averageChildrenTime = childrenTime / invocationCount;
		}

		out.println("level=" + depth);
		out.print("name=" + node.getName());

		String averageTimeString = createTimeString(averageTotalTime,
				averageThisTime, averageChildrenTime);
		out.print(averageTimeString);
		out.print(formatTime(averageTotalTime));

		if (minTime < Long.MAX_VALUE)
			out.print(" min=" + formatTime(minTime));
		if (Long.MIN_VALUE < maxTime)
			out.print(" max=" + formatTime(maxTime));
		out.print(createTimeString(totalTime, thisTime, childrenTime));
		out.print(formatTime(totalTime));
		out.print(invocationCount);

		// All children
		for (ProfilerPoint child : children)
			display(child, comparator, depth + 1);

	}
*/

	private String createTimeString(long totalTime, long thisTime,
			long childrenTime) {
		StringBuffer sb = new StringBuffer();
		sb.append(" totalTime=" + totalTime);
		sb.append(" thisTime=" + thisTime);
		sb.append(" childrenTime=" + childrenTime);
		return sb.toString();
	}

	private String formatTime(long nanoseconds) {
		StringBuffer sb = new StringBuffer();

		long milliseconds = nanoseconds / 1000000;
		long minutes = milliseconds / 1000 / 60;
		if (minutes > 0) {
			sb.append(minutes + ":");
			milliseconds -= minutes * 60 * 1000;
		}
		long seconds = milliseconds / 1000;
		if (seconds > 0)
			sb.append(seconds);
		milliseconds -= seconds * 1000;
		sb.append("." + milliseconds);

		return sb.toString();
	}

}
