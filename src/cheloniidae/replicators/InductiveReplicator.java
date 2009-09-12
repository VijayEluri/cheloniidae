package cheloniidae.replicators;

import cheloniidae.*;
import cheloniidae.commands.*;

public class InductiveReplicator<T extends Turtle> extends Replicator {
  public final TurtleCommand   step;
  public final int             copies;
  public final CommandSequence actions;
  public InductiveReplicator (final int _copies, final TurtleCommand _step, final TurtleCommand ... _actions)
    {step = _step; copies = _copies; actions = new CommandSequence (_actions);}

  public TurtleGroup<Turtle> replicate (final Turtle turtle) {
    final TurtleGroup<Turtle> result   = new TurtleGroup<Turtle> ();
    Turtle                    previous = turtle;
    for (int i = 0; i < copies; ++i) result.turtles ().add (previous = previous.clone ().run (step));
    return result;
  }

  public TurtleCommand applyTo (final Turtle turtle) {
    final TurtleGroup<Turtle> copies = this.replicate (turtle);
    turtle.window ().add (copies);      // Very important! Otherwise the copies will have no way of producing visible lines.
    copies.run (actions);
    return this;
  }

  public TurtleCommand map (final Transformation<TurtleCommand> t) {
    final TurtleCommand newCommand = t.transform (this);
    if (newCommand == this) return new InductiveReplicator (copies, step.map (t), actions.map (t));
    else                    return newCommand;
  }
}
