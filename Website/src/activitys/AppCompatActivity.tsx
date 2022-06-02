import Constants from "@Native/Constants";
import { PureComponent } from "react";
import { Page } from "react-onsenui";

class AppCompatActivity<P = {}, S = {}> extends PureComponent<P, S> {
  public readonly isAndroid: bool = Constants.isAndroid;

  public constructor(props: P | Readonly<P>) {
    super(props);
    this.onlyAndroid();
  }

  private onlyAndroid(): void {}

  public componentDidMount(): void {
    this.onlyAndroid();
  }

  public componentDidUpdate(): void {
    this.onlyAndroid();
  }

  public componentWillUnmount(): void {}

  /**
   * Sets an custom status bar color for the activity
   * @deprecated
   */
  protected setStatusbarColor(): string {
    return "#4a148c";
  }

  /**
   * Creates the activity
   */
  protected onCreate(): JSX.Element {
    return <></>;
  }

  /**
   * Renders the Toolbar
   */
  protected onCreateToolbar(): JSX.Element {
    return <></>;
  }

  protected onCreateModal(): JSX.Element {
    return <></>;
  }

  protected onCreateBottomToolbar(): JSX.Element {
    return <></>;
  }

  protected onCreateFAB(): JSX.Element {
    return <></>;
  }

  protected onInit(): void {}

  protected onShow(): void {}

  protected onHide(): void {}

  protected onInfiniteScroll(): void {}

  protected get pageModifier(): string {
    return "";
  }

  /**
   * @deprecated
   */
  public render(): JSX.Element {
    return (
      <Page
        modifier={this.pageModifier}
        renderBottomToolbar={this.onCreateBottomToolbar}
        renderFixed={this.onCreateFAB}
        renderModal={this.onCreateModal}
        onInfiniteScroll={this.onInfiniteScroll}
        onHide={this.onHide}
        onShow={this.onShow}
        onInit={this.onInit}
        renderToolbar={this.onCreateToolbar}
      >
        <this.onCreate />
      </Page>
    );
  }
}

export default AppCompatActivity;
