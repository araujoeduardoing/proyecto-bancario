export interface UIState {
  loading: boolean;
  error: string | null;
  searchText: string;
  showForm: boolean;
  creating: boolean;
  editing: boolean;
  editingClientId: number | null;
  deleting: number | null;
}

export interface AsyncOperation {
  loading: boolean;
  error: string | null;
}
